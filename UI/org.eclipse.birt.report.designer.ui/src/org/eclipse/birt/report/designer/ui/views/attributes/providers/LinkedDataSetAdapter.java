package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.data.adapter.api.LinkedDataSetUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.ReportItemHandle;

public class LinkedDataSetAdapter {
	
	public List<String> getVisibleLinkedDataSets() {
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter( this,
				ILinkedDataSetHelper.class );
		if(helper != null)
		{
			return helper.getVisibleLinkedDataSets();
		}
		return new ArrayList<String>();
	}
	
	
	public boolean setLinkedDataModel(ReportItemHandle handle, Object value)
	{
		
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter( this,
				ILinkedDataSetHelper.class );
		if(helper != null)
		{
			return helper.setLinkedDataModel(handle, value);
		}
		return false;
	}
	
	public static boolean bindToLinkedDataSet( ReportItemHandle reportItemHandle )
	{
		boolean result = false;
		try {
			result = LinkedDataSetUtil.bindToLinkedDataSet(reportItemHandle);
		} catch (Exception e) {
			ExceptionHandler.handle( e );
		} 
		return result;
	}
	
}