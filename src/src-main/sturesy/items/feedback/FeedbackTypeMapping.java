package sturesy.items.feedback;

import org.json.JSONObject;
import sturesy.feedback.editcontroller.FeedbackEditControllerBasic;
import sturesy.feedback.editcontroller.FeedbackEditControllerChoice;
import sturesy.feedback.editcontroller.IFeedbackEditController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Used for associating/mapping the different feedback types to it's feedback strings
 * and editor panels.
 *
 * Created by henrik on 6/4/14.
 */
public class FeedbackTypeMapping {
    private static final Map<Class<? extends AbstractFeedbackType>, IFeedbackEditController> typeClassMap;

    static
    {
        typeClassMap = new HashMap<>();

	    // this is where feedback types are mapped to their edit controllers
        typeClassMap.put(FeedbackTypeComment.class, new FeedbackEditControllerBasic());
        typeClassMap.put(FeedbackTypeGrades.class, new FeedbackEditControllerBasic());
        typeClassMap.put(FeedbackTypeLikert.class, new FeedbackEditControllerBasic());
        typeClassMap.put(FeedbackTypeChoice.class, new FeedbackEditControllerChoice());
    }

    /**
     * Returns the controller object associated with a feedback type.
     * @param type Class Object for Feedback Type
     * @return Controller Object for Feedback Type
     */
    public static IFeedbackEditController getControllerForTypeClass(Class<? extends AbstractFeedbackType> type)
    {
        if(typeClassMap.containsKey(type))
            return typeClassMap.get(type);
        return null;
    }

    /**
     * Returns an object for the machine readable feedback type (as stored in the database)
     * @param type Machine readable feedback type string
     * @return Newly instantiated object
     */
    public static AbstractFeedbackType instantiateObjectForType(String type)
    {
        for(AbstractFeedbackType o : getAllFeedbackTypes())
        {
            if(type.equals(o.getType()))
                return o;
        }
        return null;
    }

    /**
     * Creates a machine-readable object from JSON sheet data
     * @param obj JSON data
     * @return Newly created object
     */
    public static AbstractFeedbackType instantiateAndInitializeWithJson(JSONObject obj)
    {
        // extract json data
        if(obj.has("type")) {
            AbstractFeedbackType mo = FeedbackTypeMapping.instantiateObjectForType(obj.getString("type"));
            if (mo != null) {
                mo.setTitle(obj.getString("title"));
                mo.setDescription(obj.getString("description"));
                mo.setMandatory(obj.getInt("mandatory") == 1);
                mo.setExtra(obj.getString("extra"));

                if(obj.has("fbid"))
                    mo.setId(obj.getInt("fbid"));

                return mo;
            }
        }
        return null;
    }

    /**
     * @return A List with objects of all available feedback types
     */
    public static List<AbstractFeedbackType> getAllFeedbackTypes()
    {
        List<AbstractFeedbackType> list = new LinkedList<>();
        for(Class<? extends AbstractFeedbackType> c : typeClassMap.keySet())
        {
            try {
                AbstractFeedbackType instanced = c.newInstance();
                list.add(instanced);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
