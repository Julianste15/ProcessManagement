package co.unicauca.infrastructure.dependency_injection;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
public class PropertyMapping {
    private final String ATR_FILE_PATH;
    private final String ATR_FILE_NAME = "application";
    private InputStream atrInputStream;
    public PropertyMapping()
    {
        ATR_FILE_PATH = System.getProperty("user.dir")+ "/src/main/java/resources/"+ ATR_FILE_NAME + ".properties";
    }
    public void propertiesAssignament(Object prmObject)
    {
        try
        {
            Field arrFields[] = prmObject.getClass().getDeclaredFields();
            for(Field objField: arrFields)
            {
                objField.setAccessible(true);
                if(objField.isAnnotationPresent(Property.class))
                {
                    String varPropertyName = objField.getAnnotation(Property.class).property();
                    atrInputStream = new FileInputStream(ATR_FILE_PATH);
                    Properties objProperties = new Properties();
                    objProperties.load(atrInputStream);
                    objField.set(prmObject, objProperties.getProperty(varPropertyName));
                    atrInputStream.close();
                    return;
                }
            }
        }
        catch(Exception ex)
        {
            Logger.getLogger(PropertyMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
