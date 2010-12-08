/**
 * This file Copyright (c) 2005-2010 Aptana, Inc. This program is
 * dual-licensed under both the Aptana Public License and the GNU General
 * Public license. You may elect to use one or the other of these licenses.
 * 
 * This program is distributed in the hope that it will be useful, but
 * AS-IS and WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, TITLE, or
 * NONINFRINGEMENT. Redistribution, except as permitted by whichever of
 * the GPL or APL you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or modify this
 * program under the terms of the GNU General Public License,
 * Version 3, as published by the Free Software Foundation.  You should
 * have received a copy of the GNU General Public License, Version 3 along
 * with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Aptana provides a special exception to allow redistribution of this file
 * with certain other free and open source software ("FOSS") code and certain additional terms
 * pursuant to Section 7 of the GPL. You may view the exception and these
 * terms on the web at http://www.aptana.com/legal/gpl/.
 * 
 * 2. For the Aptana Public License (APL), this program and the
 * accompanying materials are made available under the terms of the APL
 * v1.0 which accompanies this distribution, and is available at
 * http://www.aptana.com/legal/apl/.
 * 
 * You may view the GPL, Aptana's exception and additional terms, and the
 * APL in the file titled license.html at the root of the corresponding
 * plugin containing this source file.
 * 
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.php.internal.parser;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * PHP Heredoc and Nowdoc rule.
 * 
 * @author Shalom Gibly <sgibly@aptana.com>
 */
public class HeredocRule implements IRule
{

	private IToken token;
	private int readCount;

	/**
	 * Constructs a new HeredocRule
	 */
	public HeredocRule(IToken token)
	{
		this.token = token;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner)
	{
		if (startsWithHeredoc(scanner))
		{
			// read the heredoc/nowdoc identifier (name)
			String identifier = readHeredocIdentifier(scanner);
			if (identifier != null && identifier.length() > 0)
			{
				// look for an identifier match which starts at the beginning of a line.
				findHeredocClose(identifier, scanner);
				return token;

			} else {
				for (int i = 0; i < readCount; i++)
				{
					scanner.unread();
				}
			}
		}

		return Token.UNDEFINED;
	}

	/**
	 * @param scanner
	 * @return
	 */
	private boolean startsWithHeredoc(ICharacterScanner scanner)
	{
		int heredocOpenCount = 3;
		while (heredocOpenCount > 0)
		{
			heredocOpenCount--;
			int character = scanner.read();
			if (character == ICharacterScanner.EOF)
			{
				return false;
			}
			readCount++;
			if (((char) character) != '<')
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Reads and returns the HEREDOC/NOWDOC identifier. This identifier should appear in the same line as the 'heredoc'
	 * open mark, and should not contain any white-spaces before the line terminates.
	 * 
	 * @param scanner
	 * @return A heredoc/nowdoc identifier; Null if something is wrong.
	 */
	private String readHeredocIdentifier(ICharacterScanner scanner)
	{
		StringBuilder buffer = new StringBuilder();
		String result = null;
		boolean isNowDoc = false;
		boolean hasWhiteSpace = false;
		int read = scanner.read();
		while (read != ICharacterScanner.EOF)
		{
			readCount++;
			char character = (char) read;
			if (character == '\'')
			{
				if (!isNowDoc)
				{
					if (buffer.length() > 0)
					{
						// not a valid heredoc/nowdoc
						break;
					}
					else
					{
						isNowDoc = true;
					}
				}
				else
				{
					result = buffer.toString();
				}
			}
			else if (character == '\n' || character == '\r')
			{
				if (buffer.length() > 0)
				{
					result = buffer.toString();
				}
				break;
			}
			else if (character == ' ' || character == '\t')
			{
				hasWhiteSpace = true;
			}
			else
			{
				// This character is part of the identifier
				if (hasWhiteSpace)
				{
					// non valid heredoc
					break;
				}
				buffer.append(character);
			}
			read = scanner.read();
		}
		return result;
	}

	/**
	 * Returns true if the
	 * 
	 * @param identifier
	 * @return
	 */
	private boolean findHeredocClose(String identifier, ICharacterScanner scanner)
	{
		int read = scanner.read();
		StringBuilder buffer = new StringBuilder();
		while (read != ICharacterScanner.EOF)
		{
			readCount++;
			buffer.delete(0, buffer.length());
			while (read != ICharacterScanner.EOF && scanner.getColumn() != 0)
			{
				readCount++;
				buffer.append((char) read);
				read = scanner.read();
			}
			String line = buffer.toString();
			if (line.equals(identifier) || line.equals(identifier + ';'))
			{
				return true;
			}
			read = scanner.read();
		}
		if (read == ICharacterScanner.EOF)
		{
			// We define that in case we have an illegal HEREDOC/NOWDOC, we grab to the end of the file.
			return true;
		}
		return false;
	}

}
