/*
 * Frequently used utility functions for Headmaster. This adds functions to the
 * Headmaster top-level object, if already defined.
 */
(function () {
    var PARTIAL_ISO8601 = "yyyy-MM-dd";

    window.Headmaster = $.extend(window.Headmaster || {}, {

        /*
         * Returns, as a straight-up boolean value, whether the element with the
         * given ID has a checked attribute with value "checked" (typical of
         * checkboxes and radio buttons).
         */
        isChecked: function (id) {
            return $("#" + id).attr("checked") === "checked";
        },

        /*
         * Converts a JavaScript date object into the date-only portion of its
         * ISO 8601 string equivalent. Requires Datejs.
         */
        dateToDateString: function (object, propertyName, dateFormat) {
            if (object[propertyName]) {
                object[propertyName] = object[propertyName].toString(dateFormat || PARTIAL_ISO8601);
            }
        },

        /*
         * Loads an array into a web page table. The createRow parameter is a
         * function (object) which is expected to return a jQuery element for
         * the table row that corresponds to that object.
         */
        loadArrayIntoTable: function (arrayToLoad, tableId, emptyId, createRow) {
            var table = $("#" + tableId),
                empty = $("#" + emptyId),
                tbody = table.find("tbody");

            if (arrayToLoad && arrayToLoad.length) {
                $.each(arrayToLoad, function (index, item) {
                    tbody.append(createRow(item));
                });
                table.fadeIn();
                empty.fadeOut();
            } else {
                table.fadeOut();
                empty.fadeIn();
            }
        }

    });
})();
