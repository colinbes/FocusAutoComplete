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
package liftmodules.widget

import org.specs2._
import org.specs2.SpecificationWithJUnit
import org.specs2.runner._
import org.specs2.specification.After
import com.besterdesigns.liftmodules.widget.FocusAutoComplete
import net.liftweb.http.LiftSession
import net.liftweb.util.StringHelpers
import net.liftweb.common.Empty
import net.liftweb.http.S
import net.liftweb.common.Full

class FocusAutoCompleteSpec extends SpecificationWithJUnit with After {

  val session: LiftSession = new LiftSession("", StringHelpers.randomString(20), Empty)
  val passInput =
      <span>
        <head>
          <title>Some Title</title>
          <script>Some Script</script>
        </head>
        <input type="text" id="yesthatsme"/>
        <input type="hidden" id="nopenotme"/>
      </span>
    
  val passInputSwapped =
      <span>
        <head>
          <title>Some Title</title>
          <script>Some Script</script>
        </head>
        <input type="hidden" id="nopenotme"/>
        <input type="text" id="yesthatsme"/>
      </span>

    val failInput =
      <span>
        <head>
          <title>Some Title</title>
          <script>Some Script</script>
        </head>
        <input type="dunno" id="yesthatsme"/>
        <input type="hidden" id="nopenotme"/>
      </span>
    
  def is = args(sequential = true) ^
    "With Focus on Autocomplete" ^
    "Input must focus on 'yesthatsme'" ! e1 ^
    "Swapped Input must focus on 'yesthatsme'" ! e2 ^
    "Fail Input must not focus" ! e3

  def e1 = {
    S.initIfUninitted(session) {
      val result = FocusAutoComplete(passInput)
      val unchanged = result == passInput
      val index = (S.jsToAppend).headOption match {
        case Some(x) => {
          x.toJsCmd.indexOf("""document.getElementById("yesthatsme").focus()""")
        }
        case _ => -1
      }
      index must beGreaterThan(1) and (unchanged must beTrue)
    }
  }
  
  def e2 = {
    S.initIfUninitted(session) {
      val result = FocusAutoComplete(passInputSwapped)
      val unchanged = result == passInputSwapped
      val index = (S.jsToAppend).headOption match {
        case Some(x) => {
          x.toJsCmd.indexOf("""document.getElementById("yesthatsme").focus()""")
        }
        case _ => -1
      }
      index must beGreaterThan(1) and (unchanged must beTrue)
    }
  }

  def e3 = {
    S.initIfUninitted(session) {
      val result = FocusAutoComplete(failInput)
      val unchanged = result == failInput
      val index = (S.jsToAppend).headOption match {
        case Some(x) => {
          x.toJsCmd.indexOf("""document.getElementById("yesthatsme").focus()""")
        }
        case _ => -1
      }
      index must beEqualTo(-1) and (unchanged must beTrue)
    }
  }

  def after = {
    session.destroySession
  }
}