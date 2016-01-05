'use strict';

describe('ServiceCatalogue Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockServiceCatalogue, MockProducts, MockPricing;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockServiceCatalogue = jasmine.createSpy('MockServiceCatalogue');
        MockProducts = jasmine.createSpy('MockProducts');
        MockPricing = jasmine.createSpy('MockPricing');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ServiceCatalogue': MockServiceCatalogue,
            'Products': MockProducts,
            'Pricing': MockPricing
        };
        createController = function() {
            $injector.get('$controller')("ServiceCatalogueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:serviceCatalogueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
