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
package sturesy.test.core.services.crud;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sturesy.core.backend.services.crud.QuestionCRUDService;
import sturesy.core.error.XMLException;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.items.SingleChoiceQuestion;

public class TestQuestionCRUDServiceRead
{

    private QuestionCRUDService _crudService;

    private QuestionSet _qSet;

    private File _file;

    @Before
    public void setUp() throws IOException
    {
        _crudService = new QuestionCRUDService();

        _qSet = new QuestionSet();
        for (int i = 0; i < 10; i++)
        {
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(new String[] { i + "A", "B", "C", "D" }));
            QuestionModel q = new SingleChoiceQuestion("Frage" + i, list, 1, -1);
            _qSet.addQuestionModel(q);
        }

        _file = new File("question2.xml");
        if (!_file.exists())
            _file.createNewFile();

        QuestionCRUDService writer = new QuestionCRUDService();
        writer.createAndUpdateQuestionSet(_file, _qSet);

    }

    @After
    public void tearDown()
    {
        if (_file.exists())
            _file.delete();
    }

    @Test
    public void testParseQuestionFileMultipleUnmarshal()
    {
        try
        {
            QuestionSet set = _crudService.readQuestionSet(_file);
            for (int i = 0; i < _qSet.getQuestionModels().size(); i++)
            {
                assertEquals(_qSet.getQuestionModels().get(i), set.getQuestionModels().get(i));
            }
        }
        catch (XMLException e)
        {
        }

    }
}
