/*
 * Copyright 2007-2010 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.besterdesigns.liftmodules.widget

import net.liftweb.http.S
import net.liftweb.http.js.JsCmds.Focus
import net.liftweb.http.js.JsCmds.Run
import scala.xml.{Elem, Node}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds

/**
 * Object to force focus on liftmodules' autocomplete widget. 
 * 
 * Function searches for first input field with type of text for it's id and appends javascript call to focus on id.
 * Function returns autocomplete nodeseq unaffected to make for simple inlining of function.
 * 
 * [[net.liftweb.http.js.JsCmds.FocusOnLoad]] function uses a helper function to pull (or add Id if it doesn't exist)
 * that doesn't work with xml element sequence of AutoComplete.
 * 
 * Usage:
 * {{{
 * def render = {
    val autocomplete = AutoComplete("", querydbase _, setSelected _, "placeholder" -> "search")
    
    "#ac-id" #> FocusAutoComplete(autocomplete) &
    ....
  
  }}}
 */
object FocusAutoComplete {
  def apply(in: Elem) = AutoCompleteWrapper(in, id=>Focus(id))
}

/**
 * AutoComplete wrapper that exposes input element id for generation of user javascript.
 * Usage:
 *  def render = {
 *
 *  def js(id: String): JsCmd = {
 *    val str = """
 *    |$('#""" + id + """').focus();
 *    |$('#""" + id + """').bind('blur',function() {
 *    |$(this).next().val($(this).val());
 *    |});
 * """
 *    JsCmds.Run(str.stripMargin)
 *  }
 *
 *  "#lastname" #> AutoCompleteWrapper(AutoComplete("", queryLName _, ....), id => js(id))
 *  ...} 
 */
object AutoCompleteWrapper {  
  def apply(in: Elem, code: String => JsCmd = (String) => JsCmds.Noop) = {
    findId(in) match {
      case Some(js) => {        
        S.appendJs(code(js.text))
      }
      case _ => 
    }
    in
  }

  private def findId(in: Elem): Option[Node] = {
    ((in \\ "input").filter(_ \\ "@type" exists (s => s.text == "text")) \ "@id").headOption
  }
}