import UIKit
import common

class MainViewController: UIViewController {
    @IBOutlet private weak var label: UILabel?
    private lazy var presenter: MainPresenterProtocol = MainPresenter(view: self)

    override func viewDidLoad() {
        super.viewDidLoad()

        presenter.fetchFollowers()
    }
}

extension MainViewController: MainViewProtocol {
    func showFollowers(_ followers: [User]) {
        label?.text = followers
            .map({ "\($0.login)\n" })
            .reduce("", +)
    }

    func showError(_ error: String) {
        label?.text = "Error: \(error)"
    }
}

