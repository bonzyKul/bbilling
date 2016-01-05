'use strict';

describe('Customer Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCustomer, MockCustomerAddress, MockCountry;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCustomer = jasmine.createSpy('MockCustomer');
        MockCustomerAddress = jasmine.createSpy('MockCustomerAddress');
        MockCountry = jasmine.createSpy('MockCountry');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Customer': MockCustomer,
            'CustomerAddress': MockCustomerAddress,
            'Country': MockCountry
        };
        createController = function() {
            $injector.get('$controller')("CustomerDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:customerUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
