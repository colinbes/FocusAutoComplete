FocusAutoComplete
=================

Helper function for liftmodule's autocomplete widget to force focus on autocomplete element.

Lift's JsCmds.FocusOnLoad utilizes a findOrAddId function that doesn't support autocomplete's element structure. The autocomlete widget wraps element sequence in an <span> tag which ends up being recipient of new id and focus as opposed to the actual input tag (type="text").

Usage is the same as the FocusOnLoad function. After importing FocusAutoComplete, wrap the autocomplete call/nodeseq and bind as usual.

E.g.,
def render = {
    val autocomplete = AutoComplete("", querydbase _, setSelected _, "placeholder" -> "search")
        
    "#lastname" #> FocusAutoComplete(autocomplete) &
    ....
