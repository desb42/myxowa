xowa_implement("ext.gadget.switcher@155lf", function($, jQuery, require, module) {
    'use strict';
    $(function() {
        $.each(document.querySelectorAll('.switcher-container'), function(i) {
            var activeElement, switchers = [], container = this, radioName = 'switcher-' + i;
            $.each(this.children, function() {
                var $radio, switcher = this, $labelContainer = $(switcher.querySelector('.switcher-label')), $labelText = $labelContainer.contents();
                if (!$labelText.length) {
                    return;
                }
                switchers.push(switcher);
                $radio = $('<input type="radio">').attr('name', radioName).click(function() {
                    $(activeElement).hide();
                    $(switcher).show();
                    activeElement = switcher;
                });
                if (!activeElement) {
                    activeElement = switcher;
                    $radio.prop('checked', true);
                } else if ($labelContainer.attr('data-switcher-default') !== undefined) {
                    $radio.click();
                } else {
                    $(switcher).hide();
                }
                $('<label style="display:block"></label>').append($radio, $labelText).appendTo(container);
                $labelContainer.remove();
            });
            if (switchers.length > 1) {
                $('<label style="display:block">Show all</label>').prepend($('<input type="radio">').attr('name', radioName).click(function() {
                    $(switchers).show();
                    activeElement = switchers;
                })).appendTo(container);
            }
            if (switchers.length === 1) {
                $radio.remove();
            }
        });
    });
}, null, null, null);
