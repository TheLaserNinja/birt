/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.ICommandParameterNameContants;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;

/**
 * Applies style to selected elements.
 */

public class ApplyStyleAction extends DynamicItemAction
{

	/** action ID */
	public static final String ID = "ApplyStyleAction"; //$NON-NLS-1$

	private SharedStyleHandle handle;

	private List selectionHandles;

	/**
	 * @param handle
	 */
	public ApplyStyleAction( SharedStyleHandle handle )
	{
		this.handle = handle;
		setId( ID );
		if ( handle == null )
		{
			setText( Messages.getString( "ApplyStyleAction.actionLabel.none" ) ); //$NON-NLS-1$
		}
		else
		{
			SlotHandle slotHandle = handle.getContainerSlotHandle( );

			if ( slotHandle != null
					&& slotHandle.getElementHandle( ) instanceof ThemeHandle )
			{
				setText( ( (ThemeHandle) handle.getContainerSlotHandle( )
						.getElementHandle( ) ).getName( )
						+ "." //$NON-NLS-1$
						+ DEUtil.getEscapedMenuItemText( DEUtil.getDisplayLabel( handle,
								false ) ) );
			}
			else
			{
				setText( DEUtil.getEscapedMenuItemText( DEUtil.getDisplayLabel( handle,
						false ) ) );
			}
		}
	}

	/**
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled( )
	{
		List handles = getElementHandles( );
		if ( handles.isEmpty( ) )
			return false;
		for ( int i = 0; i < handles.size( ); i++ )
		{
			if ( !( handles.get( i ) instanceof DesignElementHandle ) )
			{
				return false;
			}

			DesignElementHandle handle = (DesignElementHandle) handles.get( i );
			if ( handle != null && handle.getDefn( ).hasStyle( ) == false )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{

		if(handle != null)
		{
			CommandUtils.setVariable( ICommandParameterNameContants.STYLE_HANDLE_NAME,
					handle );
		}

		CommandUtils.setVariable( ICommandParameterNameContants.APPLAY_STYLE_ACTION_STYLE_CHECKED,
				new Boolean( isChecked( ) ) );
		try
		{
			CommandUtils.executeCommand( "org.eclipse.birt.report.designer.ui.command.applyStyleCommand",
					null );
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

		// CommandStack stack = SessionHandleAdapter.getInstance( )
		// .getCommandStack( );
		// stack.startTrans( "Te" );
		//
		// boolean isChecked = true;
		// SharedStyleHandle handle = null;
		//
		// try
		// {
		// List handles = getElementHandles( );
		// for ( int i = 0; i < handles.size( ); i++ )
		// {
		// ( (DesignElementHandle) handles.get( i ) ).setStyle( isChecked ?
		// handle
		// : null );
		// }
		// stack.commit( );
		// }
		// catch ( StyleException e )
		// {
		// stack.rollbackAll( );
		// ExceptionHandler.handle( e );
		// }

	}

	/**
	 * Gets models of selected elements
	 * 
	 */
	protected List getElementHandles( )
	{
		if ( selectionHandles == null )
		{
			selectionHandles = InsertInLayoutUtil.editPart2Model( TableUtil.filletCellInSelectionEditorpart( getSelection( ) ) )
					.toList( );
		}
		return selectionHandles;
	}
}