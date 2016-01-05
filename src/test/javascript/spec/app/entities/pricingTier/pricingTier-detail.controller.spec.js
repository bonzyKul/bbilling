'use strict';

describe('PricingTier Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPricingTier, MockPricing;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPricingTier = jasmine.createSpy('MockPricingTier');
        MockPricing = jasmine.createSpy('MockPricing');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PricingTier': MockPricingTier,
            'Pricing': MockPricing
        };
        createController = function() {
            $injector.get('$controller')("PricingTierDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:pricingTierUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
