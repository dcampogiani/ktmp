import Foundation
import common

protocol MainViewProtocol: AnyObject {
    func showFollowers(_ followers: [User])
    func showError(_ error: String)
}

protocol MainPresenterProtocol {
    func fetchFollowers()
    func onFetchFollowersSuccess(followers: AnyObject)
    func onFetchFollowersError(error: String)
}
