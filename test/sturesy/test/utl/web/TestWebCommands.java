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
package sturesy.test.utl.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import sturesy.items.SingleChoiceQuestion;
import sturesy.util.web.WebCommands;

public class TestWebCommands
{
    @Test
    public void testCleanCommand()
    {
        String lecturename = "name";
        String pw = "pw";
        String cleanCommand = WebCommands.cleanCommand(lecturename, pw);
        assertEquals("command=clean&name=" + lecturename + "&pwd=" + pw, cleanCommand);
    }

    @Test
    public void testGetVotesCommand()
    {
        String lecturename = "name";
        String pw = "pw";
        String getVotesCommand = WebCommands.getVotesCommand(lecturename, pw);
        assertEquals("command=get&name=" + lecturename + "&pwd=" + pw, getVotesCommand);
    }

    @Test
    public void testgetJSONAnswerTexts()
    {
        List<String> stringlist = Arrays.asList(new String[] { "A", "B", "C", "D" });

        String expected = "{\"0\":\"A\",\"1\":\"B\",\"2\":\"C\",\"3\":\"D\"}";

        String actual = WebCommands.getJSONAnswerTexts(stringlist).replace(" ", "").replace("\n", "");

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateTypeCommand()
    {
        String lecturename = "somelecture";
        String password = "somepassword";

        SingleChoiceQuestion model = new SingleChoiceQuestion("questions", Arrays.asList(new String[] { "A", "B" }), 0, -1);

        String expected = "command=update&name=" + lecturename + "&pwd=" + password + "&type="
                + (model.hasCorrectAnswer() ? "1" : "-1") + "&count=" + model.getAnswerSize() + "&text="
                + model.getQuestion() + "&answers="
                + WebCommands.encode(WebCommands.getJSONAnswerTexts(model.getAnswers()));

        assertEquals(expected, WebCommands.updateAnswerCommand(lecturename, password, model));
    }

    @Test
    public void testRedeemToken()
    {
        String token = "token";

        assertEquals("command=redeem&token=" + token, WebCommands.redeemToken(token));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLectureInfo()
    {
        Collection<String> mock = mock(Collection.class);
        when(mock.toString()).thenReturn("A ist lecture");
        String lectureListInfo = WebCommands.getLectureListInfo(mock);

        assertEquals("command=lectureinfo&lectures=istlectur", lectureListInfo);
    }

    @Test
    public void testEncode()
    {

    }
}
