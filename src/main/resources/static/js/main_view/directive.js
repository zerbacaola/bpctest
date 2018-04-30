'use strict';

mainModule.directive('bpcStDecorate', function() {
    return {
        restrict : 'A',
        require : '^stTable',
        scope : {
            bpcStDecorate : '@',
            bpcStDecorateFn : '='
        },

        link : function (scope, element, attributes, stController) {
            if (angular.isDefined(scope.bpcStDecorateFn) && typeof scope.bpcStDecorateFn === 'function') {
                for (var fn in stController) {
                    if (stController.hasOwnProperty(fn) && typeof stController[fn] === 'function' && fn === scope.bpcStDecorate) {
                        const commonFn = stController[fn];
                        stController[fn] = function() {
                            // TODO : implement decoration mode
                            commonFn.apply(stController, arguments);
                            scope.bpcStDecorateFn.apply(null, arguments);
                        };
                    }
                }
            }
        }
    };
});