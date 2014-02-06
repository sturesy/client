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
package sturesy.settings.websettings;

import java.util.Vector;

import sturesy.core.ui.UneditableTableModel;

public class PasswordTableModel extends UneditableTableModel
{
    private static final long serialVersionUID = 3252478574916068063L;

    private boolean _passwordvisible;

    private final int _columnToObfuscate;

    public PasswordTableModel(Object[][] tablevalues, Object[] strings, int columnToObfuscate)
    {
        super(tablevalues, strings);
        _columnToObfuscate = columnToObfuscate;
    }

    public PasswordTableModel(Vector<?> data, Vector<?> columnNames, int columnToObfuscate)
    {
        super(data, columnNames);
        _columnToObfuscate = columnToObfuscate;
    }

    public void setPasswordVisible(boolean visible)
    {
        _passwordvisible = visible;
    }
    
    @Override
    public Object getValueAt(int row, int column)
    {
        Object result = super.getValueAt(row, column);

        if (column == _columnToObfuscate && !_passwordvisible)
        {
            result = "*******";
        }

        return result;
    }

}
