package br.com.tamandua.view;

/**
 * @author Everton Rosario
 */
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;
import org.springframework.web.servlet.view.velocity.VelocityView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

public class VelocityToolboxViewResolver extends VelocityViewResolver {
    private String toolboxConfigLocation;

    public VelocityToolboxViewResolver() {
        toolboxConfigLocation = null;
        setViewClass(VelocityToolboxView.class);
    }

    public void setToolboxConfigLocation(String toolboxConfigLocation) {
        this.toolboxConfigLocation = toolboxConfigLocation;
    }

    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        VelocityView view = (VelocityView) super.buildView(viewName);
        if (toolboxConfigLocation != null)
            ((VelocityToolboxView) view).setToolboxConfigLocation(toolboxConfigLocation);
        return view;
    }

}
