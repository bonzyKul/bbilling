'use strict';

describe('Accounts Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAccounts, MockProducts, MockBranch, MockBank, MockCountry, MockCurrency, MockCustomer, MockAccountFamily;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAccounts = jasmine.createSpy('MockAccounts');
        MockProducts = jasmine.createSpy('MockProducts');
        MockBranch = jasmine.createSpy('MockBranch');
        MockBank = jasmine.createSpy('MockBank');
        MockCountry = jasmine.createSpy('MockCountry');
        MockCurrency = jasmine.createSpy('MockCurrency');
        MockCustomer = jasmine.createSpy('MockCustomer');
        MockAccountFamily = jasmine.createSpy('MockAccountFamily');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Accounts': MockAccounts,
            'Products': MockProducts,
            'Branch': MockBranch,
            'Bank': MockBank,
            'Country': MockCountry,
            'Currency': MockCurrency,
            'Customer': MockCustomer,
            'AccountFamily': MockAccountFamily
        };
        createController = function() {
            $injector.get('$controller')("AccountsDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:accountsUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
