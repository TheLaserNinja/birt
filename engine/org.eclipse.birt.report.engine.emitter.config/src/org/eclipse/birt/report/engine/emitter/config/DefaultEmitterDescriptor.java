package org.eclipse.birt.report.engine.emitter.config;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.birt.core.framework.Platform;


public abstract class DefaultEmitterDescriptor
{

	protected Map initParams = null;
	protected Locale locale;
	protected Map<String, RenderOptionDefn> renderOptionDefns = new HashMap<String, RenderOptionDefn>( );
	protected IConfigurableOption[] options;
	private boolean enabled = true;

	public void setInitParameters( Map params )
	{
		this.initParams = params;
	}

	public void setLocale( Locale locale )
	{
		if ( this.locale != locale )
		{
			this.locale = locale;
			initOptions( );
		}
	}

	protected abstract void initOptions( );
	
	public boolean isEnabled( )
	{
		return enabled;
	}
	
	public IConfigurableOptionObserver createOptionObserver( )
	{
		return null;
	}

	public String getDescription( )
	{
		return null;
	}

	public String getDisplayName( )
	{
		return null;
	}

	public String getID( )
	{
		return null;
	}
	
	void setEnabled( boolean enabled )
	{
		this.enabled = enabled;
	}
	
	void addRenderOption( String name, RenderOptionDefn renderOptionDefn )
	{
		renderOptionDefns.put( name, renderOptionDefn );
	}

	protected void applyDefaultValues( )
	{
		// parse the default value from the config file first.
		for ( IConfigurableOption option : options )
		{
			applyDefaultValue( option );
		}
	}

	protected boolean loadDefaultValues( String bundleName )
	{
		return doLoadDefaultvalues( bundleName, new RenderOptionParser( this ) );
	}

	boolean doLoadDefaultvalues( String bundleName, RenderOptionParser parser )
	{
		try
		{
			parser.parseConfigFor( bundleName );
		}
		catch ( Exception e )
		{
			renderOptionDefns = null;
		}
		return renderOptionDefns != null && !renderOptionDefns.isEmpty( );
	}
	
	private void applyDefaultValue( IConfigurableOption option )
	{
		if ( renderOptionDefns == null || renderOptionDefns.isEmpty( ) )
		{
			return;
		}
		RenderOptionDefn defn = renderOptionDefns.get( option.getName( ) );
		if ( defn != null )
		{
			String value = defn.getValue( );
			ConfigurableOption optionImpl = (ConfigurableOption) option;
			optionImpl.setEnabled( defn.isEnabled( ) );
			switch ( option.getDataType( ) )
			{
				case STRING :
					optionImpl.setDefaultValue( value );
					break;
				case BOOLEAN :
					optionImpl.setDefaultValue( Boolean.valueOf( value ) );
					break;
				case INTEGER :
					Integer intValue = null;
					try
					{
						intValue = Integer.decode( value );
					}
					catch ( NumberFormatException e )
					{
						break;
					}
					optionImpl.setDefaultValue( intValue );
					break;
				case FLOAT :
					Float floatValue = null;
					try
					{
						floatValue = Float.valueOf( value );
					}
					catch ( NumberFormatException e )
					{
						break;
					}
					optionImpl.setDefaultValue( floatValue );
					break;
				default :
					break;
			}
		}
	}

	protected URL getResourceURL( String bundleName, String resourceName )
	{
		IBundle bundle = Platform.getBundle( bundleName ); //$NON-NLS-1$
		if ( bundle != null )
		{
			return bundle.getEntry( resourceName );
		}
		return null;
	}
}