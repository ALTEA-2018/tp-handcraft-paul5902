import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.altea.bo.annotation.Controller;
import com.miage.altea.bo.annotation.RequestMapping;
import com.miage.altea.bo.controller.PokemonTypeController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private Map<String, Method> uriMappings = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Getting request for " + req.getRequestURI());
        String uri = req.getRequestURI();
        if(uriMappings.containsKey(uri)) {
            Method met = uriMappings.get(uri);
            Class<?> controller = met.getDeclaringClass();

            Map<String,String[]> parameters = req.getParameterMap();
            try {
                    if(!parameters.isEmpty()) {
                        Object result = met.invoke(controller.newInstance(),parameters);
                        ObjectMapper mapper = new ObjectMapper();
                        resp.getWriter().print(mapper.writeValueAsString(result));
                    }
                    else {
                        Object result = met.invoke(controller.newInstance());
                        ObjectMapper mapper = new ObjectMapper();
                        resp.getWriter().print(mapper.writeValueAsString(result));
                    }
                }

            catch (Exception e) {
                resp.sendError(500, "exception when calling method " + met.getName() + " : " + e.getCause().getMessage());
            }

        }
        else {
            resp.sendError(404,"no mapping found for request uri /test");
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // on enregistre notre controller au démarrage de la servlet
        this.registerController(HelloController.class);
        this.registerController(PokemonTypeController.class);
    }

    protected void registerController(Class controllerClass){
        System.out.println("Analysing class " + controllerClass.getName());
        Annotation annotation = controllerClass.getAnnotation(Controller.class);
        if(annotation != null) {
            Method[] method = controllerClass.getDeclaredMethods();
            for(Method met : method) {
                if(!met.getReturnType().getName().equals("void")) {
                    registerMethod(met);
                }
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    protected void registerMethod(Method method) {
        System.out.println("Registering method " + method.getName());
        Annotation annotation = method.getAnnotation(RequestMapping.class);
        if(annotation != null) {
            uriMappings.put(((RequestMapping) annotation).uri(),method);
        }

    }

    protected Map<String, Method> getMappings(){
        return this.uriMappings;
    }

    protected Method getMappingForUri(String uri){
        return this.uriMappings.get(uri);
    }
}
