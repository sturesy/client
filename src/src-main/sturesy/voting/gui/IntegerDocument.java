package sturesy.voting.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class IntegerDocument extends PlainDocument
{
    private static final long serialVersionUID = 1L;

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
    {
        try
        {
            Integer.parseInt(str);
            super.insertString(offs, str, a);
        } 
        catch (Exception e)
        {
        }
    }

}
