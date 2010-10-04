/**
 * Copyright (c) 2005-2008 Aptana, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.aptana.editor.php.internal.parser.nodes;

import org.eclipse.php.internal.core.documentModel.phpElementData.IPHPDocBlock;

import com.aptana.editor.php.internal.parser.PHPMimeType;
import com.aptana.parsing.ast.INameNode;
import com.aptana.parsing.ast.ParseNode;

/**
 * PHP base ParseNode
 * 
 * @author Shalom Gibly <sgibly@aptana.com>
 */
public class PHPBaseParseNode extends ParseNode implements IPHPParseNode
{
	private static final String EMPTY = " "; //$NON-NLS-1$
	protected String name;
	private int modifiers;
	private IPHPDocBlock documentation;
	private short nodeType;
	private INameNode nameNode;

	/**
	 * Constructs a new PHPBaseParseNode
	 */
	public PHPBaseParseNode()
	{
		super(PHPMimeType.MimeType);
		name = EMPTY;
	}

	/**
	 * Constructs a new PHPBaseParseNode
	 * 
	 * @param nodeType
	 * @param modifiers
	 * @param startOffset
	 * @param endOffset
	 * @param name
	 */
	public PHPBaseParseNode(short nodeType, int modifiers, int startOffset, int endOffset, String name)
	{
		super(PHPMimeType.MimeType);
		this.nodeType = nodeType;
		this.name = name.length() != 0 ? name : " "; //$NON-NLS-1$
		// this.startOffset = startOffset;
		// this.endOffset = endOffset >= startOffset ? endOffset : startOffset;
		this.start = startOffset;
		this.end = endOffset >= startOffset ? endOffset : startOffset;
		this.modifiers = modifiers;
	}

	/**
	 * @param docInfo
	 */
	public void setDocumentation(IPHPDocBlock docInfo)
	{
		this.documentation = docInfo;
	}

	/**
	 * @return documentation block or null
	 */

	public IPHPDocBlock getDocumentation()
	{
		return documentation;
	}

	/**
	 * @return node name
	 */
	public String getNodeName()
	{
		return name;
	}

	/**
	 * Set the node name.
	 * 
	 * @param name
	 */
	protected void setNodeName(String name)
	{
		this.name = name;
	}

	/**
	 * Determines if this is an empty node
	 * 
	 * @return Returns true if this is an empty node
	 */
	public boolean isEmpty()
	{
		return getChildCount() == 0;
	}

	/**
	 * @return modifiers
	 */
	public int getModifiers()
	{
		return modifiers;
	}

	/**
	 * Set the parse node's modifiers.
	 * 
	 * @param modifiers
	 */
	public void setModifiers(int modifiers)
	{
		this.modifiers = modifiers;
	}

	/**
	 * Returns the type of this node.
	 */
	public short getType()
	{
		return nodeType;
	}

	/**
	 * @param endOffset
	 */
	public void setEndOffset(int endOffset)
	{
		this.end = endOffset;
		// Reset the name node
		this.nameNode = null;
	}

	/**
	 * @param startOffset
	 */
	public void setStartOffset(int startOffset)
	{
		this.start = startOffset;
		// Reset the name node
		this.nameNode = null;
	}

	public boolean containsOffset(int offset)
	{
		if (getStart() <= offset && getEnd() >= offset)
		{
			return true;
		}
		return false;
	}

	public String toString()
	{
		return getNodeName();
	}

	public void setNameNode(String name, int startOffset, int endOffset)
	{
		this.nameNode = new NameNode(name, startOffset, endOffset);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.parsing.ast.ParseBaseNode#getNameNode()
	 */
	@Override
	public INameNode getNameNode()
	{
		if (this.nameNode == null)
		{
			this.nameNode = super.getNameNode();
		}
		return this.nameNode;
	}

	@Override
	public String getText()
	{
		if (this.nameNode == null)
		{
			return super.getText();
		}
		return this.nameNode.getName();
	}

	/**
	 * Override the default ParseBaseNode implementation to add a name check.
	 */
	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj)
				&& ((PHPBaseParseNode) obj).getNameNode().getName().equals(this.getNameNode().getName());
	}

	/*
	 * (non-Javadoc)
	 * @see com.aptana.parsing.ast.ParseBaseNode#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return 31 * super.hashCode() + this.getNameNode().getName().hashCode();
	}
}
