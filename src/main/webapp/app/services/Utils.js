angular
    .module('AtlasUi')
    .factory('Utils', Utils);

function Utils() {
    return {
        encode: function (decoded) {
            return encodeURIComponent(JSON.stringify(decoded));
        },
        decode: function (encoded) {
            return JSON.parse(decodeURIComponent(encoded));
        }
    };
}