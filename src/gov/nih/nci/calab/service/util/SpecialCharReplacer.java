/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.service.util;

public class SpecialCharReplacer
{
    char[] pattern = { ' ', '/', '\\', '|', ';', ',', '!', '@',
            '(', ')', '<', '>',   '\"', '#', '$',
            '\'', '~', '{',  '}', '[', ']', '=',
            '+',  '&', '^',   '\t'};
   
    
    public String getReplacedString(String str)
    {
        String result = str;
        for (int i = 0; i < pattern.length; i++)
        {
            result = result.replace(pattern[i],'_');
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        SpecialCharReplacer scr = new SpecialCharReplacer();
        System.out.println(scr.getReplacedString("Pre-screen ing"));
    }
}
