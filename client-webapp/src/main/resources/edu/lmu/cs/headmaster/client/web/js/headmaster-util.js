/*
 * Frequently used utility functions for Headmaster. This adds functions to the
 * Headmaster top-level object, if already defined.
 */
(function () {
    var PARTIAL_ISO8601 = "yyyy-MM-dd",

        /*
         * Utility function for toggling the visibility of two alternative
         * jQuery elements, based on the given boolean.
         */
        toggleElements = function (condition, elementIfTrue, elementIfFalse) {
            if (condition) {
                elementIfFalse.fadeOut();
                elementIfTrue.fadeIn();
            } else {
                elementIfTrue.fadeOut();
                elementIfFalse.fadeIn();
            }
        },
    
        /*
         * Loads an array into a web page table. The createRow parameter is a
         * function (object) which is expected to return a jQuery element for
         * the table row that corresponds to that object, or null if that result
         * should not be added to the table.
         * 
         * We define this function as a local variable so that it is easily
         * called by other functions in the final module.
         */
        loadArrayIntoTable = function (arrayToLoad, tableId, emptyId, createRow) {
            var table = $("#" + tableId),
                empty = $("#" + emptyId),
                tbody = table.find("tbody");
    
            if (arrayToLoad && arrayToLoad.length) {
                $.each(arrayToLoad, function (index, item) {
                    tbody.append(createRow(item));
                });
            }

            toggleElements(tbody.find("tr").length, table, empty);
        };

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
         * See above.
         */
        toggleElements: toggleElements,

        /*
         * See above.
         */
        loadArrayIntoTable: loadArrayIntoTable,

        /*
         * Loads the array result of a JSON call into a web page table.
         * Essentially a decorator on loadArrayIntoTable that adds a JSON
         * connection.
         */
        loadJsonArrayIntoTable: function (uri, progressId, tableId, emptyId, createRow, data) {
            var progress = $("#" + progressId),
                empty = $("#" + emptyId),
                table = $("#" + tableId);
    
            progress.fadeIn();
            empty.fadeOut();
            table.fadeOut();
            $.getJSON(
                uri,
                data || {},
                function (array, textStatus, jqXHR) {
                    loadArrayIntoTable(array, tableId, emptyId, createRow);
                    progress.fadeOut();
                }
            );
        },

        /*
         * Sets up standard search behavior. For consistency, specific elements
         * are expected to be available, all jQuery results (i.e., results of a
         * $() call). The createRow function here plays the same role as in
         * loadArrayIntoTable.
         *
         * - searchField: the input element that takes the search query
         * - searchProgress: the element indicating that a search is in progress
         * - searchEmpty: the element indicating that a search yielded no results
         * - searchResults: the table element that will hold the search results
         * - searchUri: the service URI that returns the search results
         * - searchParameter: the name of the parameter for the search query
         */
        setupSearchField: function (searchField, searchProgress, searchEmpty, searchResults,
                searchUri, searchParameter, createRow) {
            var searchData = {},
                searchResultsBody = searchResults.find("tbody"),

                // To avoid flooding the server with partial searches, we place the
                // search call in a timeout.
                pendingSearch;

            searchField.bind("input", function (event) {
                // If there is a pending search, kill it.
                if (pendingSearch) {
                    clearTimeout(pendingSearch);
                    pendingSearch = null;
                }

                // An empty search field just clears things.
                if (!searchField.val()) {
                    searchProgress.fadeOut();
                    searchEmpty.fadeOut();
                    searchResults.fadeOut();
                    return;
                }

                // Wait a little before triggering JSON.
                pendingSearch = setTimeout(function () {
                    // First, display the progress bar.
                    searchProgress.fadeIn();
                    
                    // Make the call.
                    searchData[searchParameter] = searchField.val();
                    $.getJSON(
                        searchUri,
                        searchData,
                        function (data, textStatus, jqXHR) {
                            searchResultsBody.empty();
                            if (data.length) {
                                $.each(data, function (index, result) {
                                    searchResultsBody.append(createRow(result));
                                });
                            }

                            // We wait until we've built the table in case the
                            // caller vetoed the addition of some results (by
                            // having createRow return null);
                            toggleElements(
                                searchResultsBody.find("tr").length,
                                searchResults,
                                searchEmpty
                            );

                            searchProgress.fadeOut();
                        }
                    );
                }, 250);
            });
        }

    });
})();
