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
package sturesy.test.loaddialog;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.ui.loaddialog.LoadButtonBarListener;
import sturesy.core.ui.loaddialog.LoadButtonBarObservable;

@RunWith(MockitoJUnitRunner.class)
public class TestLoadButtonBarObservable
{
    @Mock
    private LoadButtonBarListener _listener;

    @Mock
    private File _file;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInformLoadListenerInternalFileLoaded()
    {
        LoadButtonBarObservable observable = new LoadButtonBarObservable();
        observable.informLoadListenerLoadInternalFile(_file);

        observable.registerListener(_listener);
        observable.informLoadListenerLoadInternalFile(_file);
        observable.removeListener(_listener);
        verify(_listener, times(1)).loadedInternalFile(_file);
    }

    @Test
    public void testInformLoadListenerExternalFileLoaded()
    {
        LoadButtonBarObservable observable = new LoadButtonBarObservable();

        observable.informLoadListenerLoadExternalFile(_file);
        observable.registerListener(_listener);
        observable.informLoadListenerLoadExternalFile(_file);
        observable.removeListener(_listener);
        verify(_listener, times(1)).loadedExternalFile(_file);
    }
}
