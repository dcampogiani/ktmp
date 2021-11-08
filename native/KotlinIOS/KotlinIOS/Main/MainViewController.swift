import UIKit
import common
import Combine

class MainViewController: UIViewController {
    @IBOutlet private weak var label: UILabel?
    
    private var presenter = MainPresenter()
    private var cancellables: [AnyCancellable] = []

    override func viewDidLoad() {
        super.viewDidLoad()

        presenter.fetchFollowers()
        
        presenter
            .state
            .sink(receiveValue: updateState)
            .store(in: &cancellables)
    }
}

extension MainViewController {
    func updateState(_ state: MainViewController.State) {
        switch state {
        case .loading:
            loading()
        case let .failure(error):
            failure(error)
        case let .content(list):
            content(list)
        }
        
    }
    
    func loading() {
        
    }
    
    func content(_ followers: [User]) {
        label?.text = followers
            .map({ "\($0.login)\n" })
            .reduce("", +)
    }
    
    func failure(_ error: String) {
        label?.text = "Error: \(error)"
    }
}

