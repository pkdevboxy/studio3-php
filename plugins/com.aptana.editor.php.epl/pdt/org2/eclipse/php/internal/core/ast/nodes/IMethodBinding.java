/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Zend Technologies
 *******************************************************************************/
package org2.eclipse.php.internal.core.ast.nodes;

/**
 * A method binding represents a method or constructor of a class or interface.
 * Method bindings usually correspond directly to method or
 * constructor declarations found in the source code.
 * 
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 *
 * @see ITypeBinding#getDeclaredMethods()
 * @since 2.0
 */
public interface IMethodBinding extends IFunctionBinding {
	
	/**
	 * Returns whether this binding is for a constructor or a method.
	 * 
	 * @return <code>true</code> if this is the binding for a constructor,
	 *    and <code>false</code> if this is the binding for a method
	 */ 
	public boolean isConstructor();
	
	/**
	 * Returns the type binding representing the class or interface
	 * that declares this method or constructor.
	 * 
	 * @return the binding of the class or interface that declares this method
	 *    or constructor
	 */
	public ITypeBinding getDeclaringClass();
	
	/**
	 * Returns whether this method overrides the given method,
	 * as specified in section 8.4.8.1 of <em>The Java Language 
	 * Specification, Third Edition</em> (JLS3).
	 * 
	 * @param method the method that is possibly overriden
	 * @return <code>true</code> if this method overrides the given method,
	 * and <code>false</code> otherwise
	 * @since 3.1
	 */
	public boolean overrides(IMethodBinding method);
}
