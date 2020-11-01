import Foundation
import common

struct MainPresenter: MainPresenterProtocol {
    private let api = Api()
    private weak var view: MainViewProtocol?

    init(view: MainViewProtocol?) {
        self.view = view
    }

    func fetchFollowers() {
        api
            .getUser(userName: "gioevi90")
            .flatMap(f: { self.api.getFollowers(url: $0.followersUrl) as! Request<AnyObject> })
            .execute{ result, error in
               if let e = error {
                self.onFetchFollowersError(error: e.localizedDescription )
               } else if let res = result{
                res.fold(fe: { error in self.onFetchFollowersError(error: error.errorBody) },
                fs: { success in self.onFetchFollowersSuccess(followers: success.body) })
               }
            }
    }

    func onFetchFollowersSuccess(followers: AnyObject) {
        (followers as? [User])
            .map({ view?.showFollowers($0) })
    }

    func onFetchFollowersError(error: String) {
        view?.showError(error)
    }
}



