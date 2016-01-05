'use strict';

describe('CustomerAddress Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCustomerAddress, MockCountry, MockCustomer;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCustomerAddress = jasmine.createSpy('MockCustomerAddress');
        MockCountry = jasmine.createSpy('MockCountry');
        MockCustomer = jasmine.createSpy('MockCustomer');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'CustomerAddress': MockCustomerAddress,
            'Country': MockCountry,
            'Customer': MockCustomer
        };
        createController = function() {
            $injector.get('$controller')("CustomerAddressDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:customerAddressUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
