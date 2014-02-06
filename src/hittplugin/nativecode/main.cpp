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

#include <iostream>
#include "H-ITTSDK.h"

int main (int argc, const char * argv[])
{
    
    unsigned char pBytes[10]  = {0};
    argv++;
        
    for(int i = 0; i < 10; i++)
    {     
        pBytes[i] = atoi(*argv);
        argv++;
    }
    
    
    unsigned int pid = 0;
    unsigned int pkey_code = 0;
    
    int res = hitt_inspect(pBytes, &pid, &pkey_code);
    
    std::cout << "res:" << res << "\n";
    std::cout << "pid:" << pid << "\n";
    std::cout << "key:" << pkey_code << "\n";
    
    
    return 0;
}

