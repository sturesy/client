/*
 * StuReSy - Student Response System
 * Copyright (C) 2012-2014  StuReSy-Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sturesy.qgen.qimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.JFileChooser;

import org.jfree.util.Log;
import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import sturesy.core.Operatingsystem;
import sturesy.core.Pair;
import sturesy.core.backend.filter.filechooser.ZipFileFilter;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.items.SingleChoiceQuestion;

/**
 * XML-Parser for QTI-formatted files
 * 
 * @author w.posdorfer
 * 
 */
public class QTIImportController
{

    /**
     * Opens a JFileChooser dialog asking for a zipFile and then parses the
     * ZipFile for QTI-specific xml-files
     * 
     * @return a QuestionSet containing Questions or <code>null</code>
     */
    public QuestionSet getQuestions()
    {

        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ZipFileFilter());

        int result = fc.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null)
        {
            List<Question> list = parseZipFile(fc.getSelectedFile());
            return convertToQuestionSet(list);
        }
        else
        {
            return null;
        }

    }

    /**
     * Converts a List of Questions to a QuestionSet
     * 
     * @param questions
     *            to convert
     * @return a QuestionSet
     */
    private QuestionSet convertToQuestionSet(List<Question> questions)
    {
        QuestionSet set = new QuestionSet();

        for (Question question : questions)
        {
            if (question.basetype == BaseType.IDENTIFIER)
            {
                SingleChoiceQuestion model = new SingleChoiceQuestion();

                model.setQuestion(question.questiontext);
                Pair<Integer, List<String>> p = getAnswers(question);
                model.setCorrectAnswer(p.getFirst());
                model.setAnswers(p.getSecond());

                set.addQuestionModel(model);
            }
        }

        return set;
    }

    /**
     * Returns a Pair of the index of the correct answer and a List of Answers
     * 
     * @param question
     */
    private Pair<Integer, List<String>> getAnswers(Question question)
    {
        List<String> answers = new ArrayList<String>();

        int correctAnswer = QuestionModel.NOCORRECTANSWER;
        int currentAnswer = 0;

        for (String key : question.answers.keySet())
        {
            String answer = question.answers.get(key);
            Double points = question.points.get(key);
            if (points == null)
            {
                points = 0.0;
            }

            if (points > 0 && question.type != null && "single".equalsIgnoreCase(question.type))
            {
                correctAnswer = currentAnswer;
            }
            currentAnswer++;
            answers.add(answer);
        }

        return new Pair<Integer, List<String>>(correctAnswer, answers);
    }

    /**
     * Parses the selected zipFile
     * 
     * @param selectedFile
     * @return List of Questions
     */
    private List<Question> parseZipFile(File selectedFile)
    {
        ArrayList<Question> list = new ArrayList<Question>();
        try
        {
            ZipFile zipfile = new ZipFile(selectedFile);
            Enumeration<? extends ZipEntry> zipFileEntries = zipfile.entries();

            while (zipFileEntries.hasMoreElements())
            {
                ZipEntry entry = zipFileEntries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".xml") && !name.equalsIgnoreCase("imsmanifest.xml"))
                {
                    Question q = parseQuestion(zipfile, entry);
                    if (q != null)
                    {
                        list.add(q);
                    }
                }
            }
        }
        catch (ZipException e)
        {
            Log.error("Zip error", e);
        }
        catch (IOException e)
        {
            Log.error("Zip error", e);
        }
        return list;
    }

    private Question parseQuestion(ZipFile zipfile, ZipEntry entry) throws IOException
    {
        String questiontext = parseQuestionText(zipfile.getInputStream(entry));

        if (questiontext != null)
        {
            Question result = loadMainParts(zipfile.getInputStream(entry));
            result.questiontext = questiontext;
            return result;
        }
        else
        {
            return null;
        }
    }

    private String parseQuestionText(InputStream instream)
    {
        StringBuffer buffer = new StringBuffer();

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(instream));

            String line = "";
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line);
            }
            reader.close();
        }
        catch (Exception e)
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e1)
                {
                }
            }
            Log.error("reader failed", e);
        }

        return replaceContent(buffer.toString());
    }

    /**
     * Replaces unnecessary Content in the questiontext
     * 
     * @param parsedString
     *            the freshly parsed String
     * @return a nice String
     */
    private String replaceContent(String parsedString)
    {
        String text = parsedString;

        if (!isSingleChoiceOnly(text))
        {
            return null;
        }

        if (text.length() > 0)
        {
            final String itemBody = "itemBody";
            int indexofBeginning = text.indexOf(itemBody) + itemBody.length();
            int indexofEnd = text.indexOf("choiceInteraction");

            if (indexofEnd < indexofBeginning)
            {
                indexofEnd = text.indexOf("/itemBody");
            }

            String bodytext = text.substring(indexofBeginning + 1, indexofEnd - 1).trim();

            if (bodytext.contains("textEntryInteraction"))
            {
                bodytext = bodytext.substring(0, bodytext.indexOf("<textEntryInteraction responseI"));
            }
            text = getEncodedString(bodytext);
        }

        text = text.replace("<p xmlns=\"\">", "<p>");
        text = text.replace("font-size: 10pt", "");
        text = text.replace("courier new", "courier");
        return text;
    }

    private boolean isSingleChoiceOnly(String text)
    {
        String[] incorrectTests = new String[] { "<assessmentTest", "<hotspotInteraction", "<matchInteraction",
                "<orderInteraction", "<hottextInteraction" };

        for (int i = 0; i < incorrectTests.length; i++)
        {
            if (text.contains(incorrectTests[i]))
            {
                return false;
            }
        }

//        String regexMultipleChoice = ".*<responseDeclaration .* cardinality=\"multiple\" .*>.*";
//        if (text.matches(regexMultipleChoice))
//        {
//            return false;
//        }

        return true;
    }

    private Question loadMainParts(InputStream instream)
    {
        try
        {
            XmlPullParser parser = new MXParser();
            parser.setInput(new InputStreamReader(instream));
            return processXML(parser);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private Question processXML(XmlPullParser xpp) throws XmlPullParserException, IOException
    {
        Question question = new Question();
        int eventType = xpp.getEventType();
        String name = "";

        do
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                name = xpp.getName();

                if (name.equals("responseDeclaration"))
                {
                    question.type = xpp.getAttributeValue(1);
                    question.basetype = BaseType.getBaseType(xpp.getAttributeValue(2));
                }
                else if (name.equals("mapEntry"))
                {
                    String key = xpp.getAttributeValue(0);
                    Double value = Double.parseDouble(xpp.getAttributeValue(1).toString());
                    question.points.put(key, value);
                }
                else if (name.equals("outcomeDeclaration"))
                {
                    if (xpp.getAttributeValue(0).toString().equals("SCORE"))
                    {
                        question.outcomeScore = xpp.getAttributeValue(1);
                    }
                }
                else if (name.equals("choiceInteraction"))
                {
                    question.maxAnswers = findMaxChoices(xpp);
                }
                else if (name.equals("simpleChoice"))
                {
                    String key = xpp.getAttributeValue(0);
                    String value = xpp.nextText();
                    question.answers.put(key, getEncodedString(value));
                }
                else if(name.equals("correctResponse"))
                {
                    System.out.println(xpp.getText()); // TODO FIX QTI correct-response
                }
            }
            else if (eventType == XmlPullParser.END_TAG)
            {
            }
            eventType = xpp.next();
        }
        while (eventType != XmlPullParser.END_DOCUMENT);

        return question;
    }

    private int findMaxChoices(XmlPullParser xpp)
    {
        for (int i = 0; i < xpp.getAttributeCount(); i++)
        {
            String name = xpp.getAttributeName(i);

            if (name.equals("maxChoices"))
            {
                return Integer.parseInt(xpp.getAttributeValue(i));
            }
        }
        return -1;
    }

    private String getEncodedString(String s)
    {
        String result = s;
        try
        {
            switch (Operatingsystem.getOS())
            {
            case WINDOWS:
                result = new String(s.getBytes(), "UTF-8");
                break;
            default:
                break;
            }
        }
        catch (UnsupportedEncodingException e)
        {
        }
        return result;
    }

    /**
     * A simple Data class to bundle information about QTI-parsed questions
     * 
     * @author w.posdorfer
     */
    public class Question
    {
        public String questiontext;
        /** SINGLE OR MULTIPLE */
        public String type;
        /** SINGLE OR MULTIPLE */
        public String outcomeScore;
        /** maps answerkey to answertext */
        public HashMap<String, String> answers;
        /** maps answerkey to answerpoints */
        public HashMap<String, Double> points;
        /** maximum allowed answers */
        public int maxAnswers;
        /** Identifier or String */
        public BaseType basetype;

        public Question()
        {
            answers = new HashMap<String, String>();
            points = new HashMap<String, Double>();
        }

        public double getPointsPerAnswer()
        {
            double result = 0;

            for (double d : points.values())
            {
                if (d > result)
                {
                    result = d;
                }
            }

            return result;
        }

        @Override
        public String toString()
        {
            return "Question [questiontext=" + questiontext + ", type=" + type + ", points=" + points + ", maxAnswers="
                    + maxAnswers + ", basetype=" + basetype + "]";
        }
    }

    public enum BaseType
    {
        IDENTIFIER, STRING, UNKNOWN;

        /**
         * Returns the {@link BaseType} of the given String
         * 
         * @param s
         *            string to check
         * @return parsed {@link BaseType}
         */
        public static BaseType getBaseType(String s)
        {
            s = s.toLowerCase();
            if (s.equals("string"))
            {
                return STRING;
            }
            else if (s.equals("identifier"))
            {
                return IDENTIFIER;
            }
            else
            {
                return UNKNOWN;
            }
        }
    }
}
