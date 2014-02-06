/*
 * StuReSy - Student Response System
 * Copyright (C) 2012-2013  StuReSy-Team
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
package sturesy.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A QuestionSet consist of a series of QuestionModels
 * 
 * @author w.posdorfer
 * 
 */
@XmlRootElement(name = "questionset")
@XmlAccessorType(XmlAccessType.NONE)
public class QuestionSet implements Iterable<QuestionModel>
{

    /** List of QuestionModels */
    @XmlElements({ @XmlElement(name = "questionmodel", type = SingleChoiceQuestion.class),
            @XmlElement(name = "multiplechoice", type = MultipleChoiceQuestion.class),
            @XmlElement(name = "textquestion", type = TextQuestion.class) })
    private List<QuestionModel> _questions;

    /**
     * Creates an emtpy Questionset
     */
    public QuestionSet()
    {
        this(new ArrayList<QuestionModel>());
    }

    /**
     * Creates a QuestionSet using the List of Questionmodels
     * 
     * @param questionModels
     *            list to use
     */
    public QuestionSet(List<QuestionModel> questionModels)
    {
        _questions = questionModels;
    }

    /**
     * Creates a QuestionSet using the array of Questionmodels
     * 
     * @param questionModels
     *            array to use
     */
    public QuestionSet(QuestionModel[] questionModels)
    {
        this(Arrays.asList(questionModels));
    }

    /**
     * Add a questionmodel to the last index
     * 
     * @param question
     *            questionmodel to add
     */
    public void addQuestionModel(QuestionModel question)
    {
        _questions.add(question);
    }

    /**
     * Removes the specified model from the list
     * 
     * @param question
     *            to be removed
     */
    public void removeQuestionModel(QuestionModel question)
    {
        _questions.remove(question);
    }

    /**
     * Returns the underlying list of Questionmodels
     * 
     * @return List of QuestionModels (mostly Arraylist)
     */
    public List<QuestionModel> getQuestionModels()
    {
        return _questions;
    }

    /**
     * Swaps two QuestionModels in this Set
     * 
     * @param i
     *            switch this with j
     * @param j
     *            switch this with i
     */
    public void swapElements(int i, int j)
    {
        Collections.swap(_questions, i, j);
    }

    /**
     * Returns the Element at given Index
     * 
     * @param i
     *            index
     * @return QuestionModel at index i
     */
    public QuestionModel getIndex(int i)
    {
        return _questions.get(i);
    }

    /**
     * Returns the amount of QuestionModels
     * 
     * @return size of Questionmodels
     */
    public int size()
    {
        return _questions.size();
    }

    /**
     * Returns the amount of answers in the Questionmodel at a specified index
     * 
     * @param index
     *            index
     * @return amount of answers
     */
    public int sizeOfAnswers(int index)
    {
        return _questions.get(index).getAnswerSize();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for (QuestionModel m : _questions)
        {
            sb.append(m.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof QuestionSet)
        {
            QuestionSet qset = (QuestionSet) obj;

            if (getQuestionModels().size() != qset.getQuestionModels().size())
            {
                return false;
            }

            for (int i = 0; i < this.getQuestionModels().size(); i++)
            {
                if (!getIndex(i).equals(qset.getIndex(i)))
                {
                    return false;
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public Iterator<QuestionModel> iterator()
    {
        return _questions.iterator();
    }
}
