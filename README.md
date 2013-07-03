FocusAutoComplete
=================

Helper function for liftmodule's autocomplete widget to force focus on autocomplete element.

Lift's JsCmds.FocusOnLoad utilizes a findOrAddId function that doesn't support autocomplete's element structure. The autocomlete widget wraps element sequence in an <span> tag which ends up being recipient of new id and focus as opposed to the actual input tag (type="text").

Two helper functions are provided. FocusAutoComplete and AutoCompleteWrapper


FocusAutoComplete
=================

Usage is the same as the FocusOnLoad function. After importing FocusAutoComplete, wrap the autocomplete call/nodeseq and bind as usual.

e.g.,

    def render = {
      val autocomplete = AutoComplete("", querydbase _, setSelected _, "placeholder" -> "search")
        
      "#lastname" #> FocusAutoComplete(autocomplete) &
    }
    
AutoCompleteWrapper
===================
    
A new AutoCompleteWrapper function exposes input's id via a callback allowing insertion of custom javascript

e.g., 

    def render = {
 
       def js(id: String): JsCmd = {
         val str = """
         |$('#""" + id + """').focus();
         |$('#""" + id + """').bind('blur',function() {
         |$(this).next().val($(this).val());
         |});
      """
         JsCmds.Run(str.stripMargin)
       }
     
       "#lastname" #> AutoCompleteWrapper(AutoComplete("", queryLName _, ....), id => js(id))
     
       ...} 
       
Thanks to Richard Dallaway for putting me onto the right path.
