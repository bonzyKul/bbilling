'use strict';

describe('Country Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCountry, MockCurrency;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCountry = jasmine.createSpy('MockCountry');
        MockCurrency = jasmine.createSpy('MockCurrency');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Country': MockCountry,
            'Currency': MockCurrency
        };
        createController = function() {
            $injector.get('$controller')("CountryDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:countryUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
