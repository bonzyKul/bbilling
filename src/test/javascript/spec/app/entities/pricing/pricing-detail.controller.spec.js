'use strict';

describe('Pricing Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPricing, MockProducts, MockCurrency, MockPricingTier, MockServiceCatalogue;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPricing = jasmine.createSpy('MockPricing');
        MockProducts = jasmine.createSpy('MockProducts');
        MockCurrency = jasmine.createSpy('MockCurrency');
        MockPricingTier = jasmine.createSpy('MockPricingTier');
        MockServiceCatalogue = jasmine.createSpy('MockServiceCatalogue');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Pricing': MockPricing,
            'Products': MockProducts,
            'Currency': MockCurrency,
            'PricingTier': MockPricingTier,
            'ServiceCatalogue': MockServiceCatalogue
        };
        createController = function() {
            $injector.get('$controller')("PricingDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:pricingUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
