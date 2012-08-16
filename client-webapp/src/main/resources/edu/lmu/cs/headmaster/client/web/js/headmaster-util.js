/*
 * Frequently used utility functions for Headmaster. This adds functions to the
 * Headmaster top-level object, if already defined.
 */
(function () {
        // Start with some "constants."
    var PARTIAL_ISO8601 = "yyyy-MM-dd",
        KEY_ARRAY_SOURCE = "arraySource",
        KEY_ARRAY_INDEX = "arrayIndex",

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
         * Fairly reusable HTML table sorter. The table parameter should be a jQuery-wrapped
         * table element, and sortHeader is the th element sitting over the column to sort.
         * For best results, use on tables that are loaded from arrays by the other functions
         * in this module, because this preloads data that sortTable uses.  If you rolled your
         * own table, store the table's model array as table.data(KEY_ARRAY_SOURCE) and the
         * original array indices as tr.data(KEY_ARRAY_INDEX).  Note also the use of i elements
         * with Bootstrap icon classes to indicate the current sort order.
         *
         * The refresh parameter indicates whether the sort order should be reused instead of
         * advanced (i.e., table data may have changed and so a sort under the current settings
         * needs to be redone).  Normally, a sort is triggered by a user clicking on a table
         * header, and in this situation you want to change the sort order to the next one in
         * the sequence.
         */
        sortTable = function (table, sortHeader, refresh) {
            // Pull out the table's body and rows.
            var tableBody = table.find("tbody"),
                tableRows = tableBody.find("tr"),
                arraySource = table.data(KEY_ARRAY_SOURCE),
                tableRowArray = tableRows.get(),
                columnIndex = table.find("thead > tr > th").get().indexOf(sortHeader[0]),

                // The comparator functions.
                ascendingColumnSort = function (row1, row2) {
                    var columnSelector = "td:eq(" + columnIndex + ")",
                        cellText1 = $(row1).find(columnSelector).text(),
                        cellText2 = $(row2).find(columnSelector).text(),

                        // Special prep based on data types.
                        preprocessCell = function (cellText) {
                            // If the cell does not coerce to a number,
                            // check if it parses as a date. Return the
                            // date if it does parse that way, and
                            // otherwise convert to lowercase (for
                            // case-insensitive string sorting).
                            var possibleNumber = +cellText,
                                possibleDate;
                            if (isNaN(possibleNumber)) {
                                possibleDate = Date.parse(cellText);
                                return possibleDate ? possibleDate : cellText.toLowerCase();
                            } else {
                                return possibleNumber;
                            }
                        },

                        value1 = preprocessCell(cellText1),
                        value2 = preprocessCell(cellText2);

                    // If the preprocessed values do not have the same type, revert to
                    // using just the cell text.
                    if (typeof value1 !== typeof value2) {
                        value1 = cellText1.toLowerCase();
                        value2 = cellText2.toLowerCase();
                    }

                    // Less-than/greater-than comparison unless both values are numbers.
                    return (value1 < value2) ? -1 : ((value1 > value2) ? 1 : 0);
                },

                descendingColumnSort = function (row1, row2) {
                    return -ascendingColumnSort(row1, row2);
                },

                originalDataSort = function (row1, row2) {
                    // This sort is based on array *indices* in the original data.
                    return +$(row1).data(KEY_ARRAY_INDEX) - $(row2).data(KEY_ARRAY_INDEX);
                },

                // Sorting operations by type.
                sortOperations = {
                    "sort-ascending": function () {
                        tableRowArray.sort(ascendingColumnSort);
                        return "icon-arrow-up";
                    },
                    
                    "sort-descending": function () {
                        tableRowArray.sort(descendingColumnSort);
                        return "icon-arrow-down";
                    },
                    
                    "sort-none": function () {
                        tableRowArray.sort(originalDataSort);
                        return null;
                    }
                },

                // Temporary/transient variables.
                i, max, newIconClass;

            // Detach the table body element from the browser,
            // and the table body's rows from the table body.
            tableBody.detach().detach("tr");

            // Determine the direction of the sort.  We cycle between no sort,
            // ascending, and descending.
            newIconClass = sortOperations[refresh ?
                (sortHeader.find("i.icon-arrow-up").length ? "sort-ascending" :
                        (sortHeader.find("i.icon-arrow-down").length ? "sort-descending" :
                                "sort-none")) :
                (sortHeader.find("i.icon-arrow-up").length ? "sort-descending" :
                        (sortHeader.find("i.icon-arrow-down").length ? "sort-none" :
                                "sort-ascending"))
            ]();

            // Reinstate the removed rows.
            for (i = 0, max = tableRowArray.length; i < max; i += 1) {
                tableBody.append(tableRowArray[i]);
            }

            // Clear all current sort states.
            table.find("th").find("i.icon-arrow-up, i.icon-arrow-down").remove();
            if (newIconClass) {
                sortHeader.append($("<i></i>").addClass(newIconClass));
            }

            // Reattach the table body element.
            tableBody.appendTo(table);
        },

        /*
         * Loads an array into a web page table. The createRow parameter is a
         * function (object) which is expected to return a jQuery element for
         * the table row that corresponds to that object, or null if that result
         * should not be added to the table.
         * 
         * By default, tables with thead > tr > th elements are sortable. To fix
         * the row order, either define tables without thead > th elements or
         * pass a truthy fixedOrder argument.
         *
         * We define this function as a local variable so that it is easily
         * called by other functions in the final module.
         */
        loadArrayIntoTable = function (arrayToLoad, tableId, emptyId, createRow, fixedOrder) {
            var table = $("#" + tableId),
                empty = $("#" + emptyId),
                tbody = table.find("tbody"),
                th = table.find("thead > tr > th");
    
            if (arrayToLoad && arrayToLoad.length) {
                // We save the original array as data on the overall table element.
                table.data(KEY_ARRAY_SOURCE, arrayToLoad);

                // Ask the caller to create the table rows. Each row also gets
                // the index of its corresponding array element saved under
                // KEY_ARRAY_INDEX data.
                $.each(arrayToLoad, function (index, item) {
                    tbody.append(createRow(item).data(KEY_ARRAY_INDEX, index));
                });

                // Set up column sorting.
                if (th.length && !fixedOrder) {
                    th.click(function (event) {
                        sortTable(table, $(this));
                    });
                }
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
         * The inverse of load array into table: this one transfers data from a
         * table (well, really, a jQuery collection that typically comes from a
         * table) into an array. The function also deletes the array if it is
         * empty, because empty arrays don't travel well to the service.
         * 
         * - owner: the object that holds the array to be loaded
         * - arrayProperty: the property name in owner for the array
         * - sourceCollection: the jQuery collection holding the data to gather (typically,
         *   a collection of tr or td, but can really be anything)
         * - getArrayElement: the function (object) that returns the element to push onto the
         *   array, given one member of sourceCollection
         */
        loadTableIntoArray: function (owner, arrayProperty, sourceCollection, getArrayElement) {
            owner[arrayProperty] = owner[arrayProperty] || [];
            sourceCollection.each(function (index, item) {
                owner[arrayProperty].push(getArrayElement(item));
            });
            if (!owner[arrayProperty].length) {
                delete owner[arrayProperty];
            }
        },

        /*
         * Loads the array result of a JSON call into a web page table.
         * Essentially a decorator on loadArrayIntoTable that adds a JSON
         * connection. Two differences: an optional data argument can be used to
         * supply service call parameters, and another optional callback
         * argument can supply, well, a callback after the array has been
         * loaded.
         */
        loadJsonArrayIntoTable: function (uri, progressId, tableId, emptyId, createRow,
                data, callback) {
            var progress = $("#" + progressId),
                empty = $("#" + emptyId),
                table = $("#" + tableId);
    
            // Allow for no data but a callback.
            if ($.isFunction(data) && !callback) {
                callback = data;
                data = null;
            }

            progress.fadeIn();
            empty.fadeOut();
            table.fadeOut();
            $.getJSON(
                uri,
                data || {},
                function (array, textStatus, jqXHR) {
                    loadArrayIntoTable(array, tableId, emptyId, createRow);
                    if ($.isFunction(callback)) {
                        callback(array, textStatus, jqXHR);
                    }
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
