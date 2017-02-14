/**
 * @providesModule react-native-social-share
 */

import { KDSocialShare } from 'NativeModules'

module.exports = {
  shareOnTwitter: function(params, callback) {
    if (!(params['link'] || params['text'])) {
      callback("missing_link_or_text");
    } else {
      return KDSocialShare.tweet(params, callback);
    }
  },
  shareOnFacebook: function(params, callback) {
    if (!(params['link'] || params['text'])) {
      callback("missing_link_or_text");
    } else {
      return KDSocialShare.shareOnFacebook(params, callback);
    }
  }
};
