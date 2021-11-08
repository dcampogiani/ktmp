import Foundation
import common

@MainActor
class MainPresenter: ObservableObject {
    private let api = Api()
    
    @Published private(set) var privateState: MainViewController.State = .loading
    var state: Published<MainViewController.State>.Publisher { $privateState }

    func fetchFollowers() {
        Task {
            await fetchFollowersAsync()
        }
    }

    func fetchFollowersAsync() async {
        do {
            try await api
                .getUser(userName: "gioevi90")
                .flatMap(f: { self.api.getFollowers(url: $0.followersUrl) as! Request<AnyObject> })
                .execute()
                .fold(fe: { self.onFetchFollowersError(error: $0.errorBody) },
                      fs: { self.onFetchFollowersSuccess(followers: $0.body) })
        } catch {
            onFetchFollowersError(error: error.localizedDescription )
        }
    }
    
    func onFetchFollowersSuccess(followers: AnyObject) {
        privateState = .content(followers as? [User] ?? [])
    }

    func onFetchFollowersError(error: String) {
        privateState = .failure(error)
    }
}

extension MainViewController {
    enum State {
        case failure(String)
        case content([User])
        case loading
    }
}

