//On load
$(function() {
    // Default: hide edit mode
    $(".editMode").hide();
    
    // Click on "selectall" box
    $("#selectall").click(function () {
        $('.cb').prop('checked', this.checked);
    });

    // Click on a checkbox
    $(".cb").click(function() {
        if ($(".cb").length == $(".cb:checked").length) {
            $("#selectall").prop("checked", true);
        } else {
            $("#selectall").prop("checked", false);
        }
        if ($(".cb:checked").length != 0) {
            $("#deleteSelected").enable();
        } else {
            $("#deleteSelected").disable();
        }
    });

});


// Function setCheckboxValues
(function ( $ ) {
    $.fn.setCheckboxValues = function(formFieldName, checkboxFieldName) {
        var str = $('.' + checkboxFieldName + ':checked').map(function() {
            return this.value;
        }).get().join();
        
        $(this).attr('value',str);
        return this;
    };
}( jQuery ));

// Function toggleEditMode
(function ( $ ) {
    $.fn.toggleEditMode = function() {
        if($(".editMode").is(":visible")) {
            $(".editMode").hide();
            $("#editComputer").text(lang['toogle.off']);
        }
        else {
            $(".editMode").show();
            $("#editComputer").text(lang['toogle.on']);
        }
        return this;
    };
}( jQuery ));

(function ( $ ) {
    $.fn.confirmDeleteCompany = function() {
       return (confirm(lang['confirm.company']));
    };
}( jQuery ));

// Function delete selected: Asks for confirmation to delete selected computers, then submits it to the deleteComputerForm
(function ( $ ) {
    $.fn.deleteSelected = function() {
        if (confirm(lang['confirm.computer'])) { 
            $('#deleteComputerForm input[name=computer_selection_delete]').setCheckboxValues('selection','cb');
            $('#deleteComputerForm').submit();
        }
    };
}( jQuery ));



//Event handling
//Onkeydown
$(document).keydown(function(e) {
    switch (e.keyCode) {
        //DEL key
        case 46:
            if($(".editMode").is(":visible") && $(".cb:checked").length != 0) {
                $.fn.deleteSelected();
            }   
            break;
        //E key (CTRL+E will switch to edit mode)
        case 69:
            if(e.ctrlKey) {
                $.fn.toggleEditMode();
            }
            break;
    }
});

