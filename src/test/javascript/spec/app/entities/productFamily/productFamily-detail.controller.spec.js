'use strict';

describe('ProductFamily Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockProductFamily, MockProducts;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockProductFamily = jasmine.createSpy('MockProductFamily');
        MockProducts = jasmine.createSpy('MockProducts');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ProductFamily': MockProductFamily,
            'Products': MockProducts
        };
        createController = function() {
            $injector.get('$controller')("ProductFamilyDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'bbillingApp:productFamilyUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
