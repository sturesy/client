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
package sturesy.util.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import sturesy.core.Localize;
import sturesy.core.Log;
import sturesy.core.plugin.IPollPlugin;
import sturesy.core.plugin.Injectable;
import sturesy.core.plugin.QuestionVoteMatcher;
import sturesy.items.LectureID;
import sturesy.items.QuestionModel;
import sturesy.items.Vote;
import sturesy.items.vote.MultipleVote;
import sturesy.items.vote.SingleVote;
import sturesy.items.vote.TextVote;
import sturesy.util.BackgroundWorker;
import sturesy.util.Settings;

/**
 * WebVotingHandler is the implementation of a Voting-Class that polls votings
 * from a Server using simple http-post commands
 * 
 * @author w.posdorfer
 * 
 */
public class WebVotingHandler implements IPollPlugin
{

    public static final String NODATA = "No Data";

    public static final LectureID NOLECTUREID = new LectureID(Localize.getString("error.no.lecture.id"),
            "this.is.no.password", "http://www.no-lecture-id.com/");

    private static final String RELAY_DATE_FORMAT = "yyyy-MM-d H:m:s";

    private Injectable _injectable;

    private TimerTask _pollingTimer;

    private Settings _settings = Settings.getInstance();

    private Timer _timer = new Timer();
    private long _startingTime;
    private int _frequency;

    private LectureID _lectureid;
    private QuestionModel _question;

    /**
     * Creates a new WebVotingHandler
     */
    public WebVotingHandler()
    {
        _frequency = _settings.getInteger(Settings.POLL_FREQUENCY);

        if (_frequency < 10)
            _frequency = 2000;
    }

    @Override
    public void setInjectable(Injectable injectable)
    {
        _injectable = injectable;
    }

    @Override
    public void startPolling()
    {
        if (_lectureid != NOLECTUREID)
        {
            _startingTime = Calendar.getInstance().getTimeInMillis();
            _timer = new Timer();
            _timer.scheduleAtFixedRate(_pollingTimer, _frequency, _frequency);
        }
    }

    @Override
    public void stopPolling()
    {
        if (_lectureid != NOLECTUREID)
        {
            _pollingTimer.cancel();
            _timer.purge();

            // gatherVotes one last time, in background
            new BackgroundWorker()
            {
                public void inBackground()
                {
                    gatherVotes();
                }
            }.execute();

            setUpTimerTask();
        }
    }

    @Override
    public void prepareVoting(LectureID lectureid, final QuestionModel model)
    {
        _question = model;
        _lectureid = lectureid;

        if (_lectureid != NOLECTUREID)
        {
            setUpTimerTask();
            new BackgroundWorker()
            {
                public void inBackground()
                {
                    clearPreviousVotings();
                    updateAnswerTypeWebsite();
                }
            }.execute();
        }
    }

    /**
     * Sends a Post-Command to the Server to request Votes
     */
    private void gatherVotes()
    {
        parseVotesJSON(WebCommands2.getVotes(_lectureid.getHost(), _lectureid.getLectureID(), _lectureid.getPassword()));
    }

    /**
     * Update the Database to know how many answers the current Question has
     */
    private void updateAnswerTypeWebsite()
    {
        WebCommands2.updateQuestion(_lectureid.getHost().toString(), _lectureid.getLectureID(),
                _lectureid.getPassword(), _question);
    }

    /**
     * Clears all previous Votes
     */
    private void clearPreviousVotings()
    {
        WebCommands2.cleanVotes(_lectureid.getHost().toString(), _lectureid.getLectureID(), _lectureid.getPassword());
    }

    private void setUpTimerTask()
    {
        if (_pollingTimer != null)
            _pollingTimer.cancel();
        _pollingTimer = new TimerTask()
        {
            public void run()
            {
                gatherVotes();
            }
        };
    }

    private long getTimeDiff(String timefromserver)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(RELAY_DATE_FORMAT);
            Date d = sdf.parse(timefromserver);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(d);

            long diff = c1.getTimeInMillis() - _startingTime;
            diff = diff < 0 ? 0 : diff;
            return diff;
        }
        catch (ParseException e)
        {
            return 0;
        }

    }

    private void parseVotesJSON(String jsonString)
    {
        LinkedList<Vote> resultList = new LinkedList<Vote>();

        if (!jsonString.startsWith("NO DATA") && jsonString.startsWith("{") && jsonString.endsWith("}"))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(jsonString);

                for (Object firstlevel : jsonObject.keySet())
                {
                    if (firstlevel instanceof String)
                    {
                        JSONObject secondlevel = jsonObject.getJSONObject((String) firstlevel);

                        String guid = secondlevel.getString("0");
                        String dateString = secondlevel.getString("2");
                        long timediff = getTimeDiff(dateString);

                        Object votes = secondlevel.get("1");

                        if (votes instanceof JSONObject)
                        {
                            JSONObject votesarray = (JSONObject) votes;
                            ArrayList<Short> voteslist = new ArrayList<Short>();

                            for (Object possibleIntKeys : votesarray.keySet())
                            {
                                Object possibleInt = votesarray.get("" + possibleIntKeys);
                                if (possibleInt instanceof Integer)
                                {
                                    voteslist.add(((Integer) possibleInt).shortValue());
                                }
                            }
                            resultList.add(new MultipleVote(guid, timediff, voteslist));
                        }
                        else if (votes instanceof Integer)
                        {
                            resultList.add(new SingleVote(guid, timediff, (Integer) votes));
                        }
                        else if (votes instanceof String)
                        {
                            resultList.add(new TextVote(guid, timediff, (String) votes));
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                Log.error(e);
            }
        }
        for (Vote vote : resultList)
        {
            if (QuestionVoteMatcher.matches(_question, vote))
            {
                _injectable.injectVote(vote);
            }
        }
    }
}
