package br.com.tamandua.view;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;

/**
 * @author Everton Rosario
 */
public class VelocityLayoutViewResolver extends VelocityToolboxViewResolver {
    private String layoutUrl;
    private String layoutKey;
    private String screenContentKey;
    private Map<String, String> mappings = new HashMap<String, String>();

    public VelocityLayoutViewResolver() {
        layoutUrl = null;
        layoutKey = null;
        screenContentKey = null;
        setMappings(null);
        setViewClass(VelocityLayoutView.class);
    }

    public void setLayoutUrl(String layoutUrl) {
        this.layoutUrl = layoutUrl;
    }

    public void setLayoutKey(String layoutKey) {
        this.layoutKey = layoutKey;
    }

    public void setScreenContentKey(String screenContentKey) {
        this.screenContentKey = screenContentKey;
    }

    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
    	VelocityLayoutView view = (VelocityLayoutView) super.buildView( viewName );
        
        if (layoutUrl != null)
            ((VelocityLayoutView) view).setLayoutUrl(layoutUrl);
        if (layoutKey != null)
            ((VelocityLayoutView) view).setLayoutKey(layoutKey);
        if (screenContentKey != null)
            ((VelocityLayoutView) view).setScreenContentKey(screenContentKey);
        if ( ! this.mappings.isEmpty() ) {
			for ( Map.Entry<String, String> entry : this.mappings.entrySet() ) {
				 
				String mappingRegexp = StringUtils.replace(entry.getKey(), "*", ".*");
				 
				if ( viewName.matches( mappingRegexp ) ) {
		
					view.setLayoutUrl( entry.getValue());

					return view;
				}
			}
		}
        return view;
    }

	public void setMappings(Map<String, String> mappings) {
		this.mappings = mappings;
	}

	public Map<String, String> getMappings() {
		return mappings;
	}

}
