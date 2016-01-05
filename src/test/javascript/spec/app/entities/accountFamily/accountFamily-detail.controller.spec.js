'use strict';

describe('AccountFamily Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAccountFamily, MockAccounts;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAccountFamily = jasmine.createSpy('MockAccountFamily');
        MockAccounts = jasmine.createSpy('MockAccounts');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'AccountFamily': MockAccountFamily,
            'Accounts': MockAccounts
        };
        createController = function() {
            $injector.get('$controller')("AccountFamilyDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:accountFamilyUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
