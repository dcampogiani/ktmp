//
//  ViewController.swift
//  KotlinIOS
//
//  Created by Daniele Campogiani on 10/07/2019.
//  Copyright © 2019 Daniele Campogiani. All rights reserved.
//

import UIKit
import common

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        let label = UILabel(frame: CGRect(x: 0, y: 0, width: 300, height: 21))
        label.center = CGPoint(x: 160, y: 285)
        label.textAlignment = .center
        label.font = label.font.withSize(25)
        label.text = CommonKt.createApplicationScreenMessage()
        view.addSubview(label)

        Api()
            .getRandomPerson()
            .executeCallback(onSuccess: { label.text = $0?.name },
                             onError: { _ in })
    }
}

