jQuery(function ($) {
            var HEADER_SELECTOR = 'header'
              , SEARCH_BOX_SELECTOR = '.vector-search-box'
              , SEARCH_VISIBLE_CLASS = 'vector-header-search-toggled';
            function bindSearchBoxHandler(searchBox, header) {
                function clickHandler(ev) {
                    if (ev.target instanceof HTMLElement && !ev.target.closest('.wvui-typeahead-suggestion') && !searchBox.contains(ev.target)) {
                        header.classList.remove(SEARCH_VISIBLE_CLASS);
                        document.removeEventListener('click', clickHandler);
                    }
                }
                document.addEventListener('click', clickHandler);
            }
            function bindToggleClickHandler(searchBox, header, searchToggle) {
                function handler(ev) {
                    ev.preventDefault();
                    header.classList.add(SEARCH_VISIBLE_CLASS);
                    setTimeout(function() {
                        bindSearchBoxHandler(searchBox, header);
                        var searchInput = (searchBox.querySelector('input[type="search"]'));
                        if (searchInput) {
                            searchInput.focus();
                        }
                    });
                }
                searchToggle.addEventListener('click', handler);
            }
            function initSearchToggle(searchToggle) {
                if (!searchToggle || !searchToggle.closest) {
                    return;
                }
                var header = (searchToggle.closest(HEADER_SELECTOR));
                if (!header) {
                    return;
                }
                var searchBox = (header.querySelector(SEARCH_BOX_SELECTOR));
                if (!searchBox) {
                    return;
                }
                bindToggleClickHandler(searchBox, header, searchToggle);
            }
            ;
            initSearchToggle(document.querySelector('.mw-header .search-toggle'));
});
