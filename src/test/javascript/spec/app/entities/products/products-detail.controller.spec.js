'use strict';

describe('Products Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockProducts, MockProductFamily, MockServiceCatalogue;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockProducts = jasmine.createSpy('MockProducts');
        MockProductFamily = jasmine.createSpy('MockProductFamily');
        MockServiceCatalogue = jasmine.createSpy('MockServiceCatalogue');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Products': MockProducts,
            'ProductFamily': MockProductFamily,
            'ServiceCatalogue': MockServiceCatalogue
        };
        createController = function() {
            $injector.get('$controller')("ProductsDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:productsUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
