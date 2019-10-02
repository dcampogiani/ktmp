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
            .executeCallback(onSuccess: { $0.fold(fe: { error in self.onFetchFollowersError(error: error.errorBody) },
                                                  fs: { success in self.onFetchFollowersSuccess(followers: success.body) }) },
                             onError: { self.onFetchFollowersError(error: $0.message ?? "")  })
    }

    func onFetchFollowersSuccess(followers: AnyObject) {
        (followers as? [User])
            .map({ view?.showFollowers($0) })
    }

    func onFetchFollowersError(error: String) {
        view?.showError(error)
    }
}



