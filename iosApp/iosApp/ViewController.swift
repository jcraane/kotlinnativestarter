import UIKit
import common

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        label.text = Proxy().proxyHello()
        button.setTitle("MyButton", for: .normal)
         button.addTarget(self, action: #selector(buttonTapped(sender:)), for: UIControlEvents.touchUpInside)
        
        let api = IosApiWrapper()
        api.retrievePersons(success: { [weak self] (persons: [Person]) in
            print("Success, got \(persons)")
            //self?.handle(persons: persons) Enable to demonstrate
        }, failure: { (throwable: KotlinThrowable?) in
            print("Error; \(throwable?.description() ?? "")")
        })
        
        api.retrieveTasks(success: { [weak self] (tasks: [Task]) in
            print("Success, got \(tasks)")
            //self?.handle(persons: persons) Enable to demonstrate
        }, failure: { (throwable: KotlinThrowable?) in
            print("Error; \(throwable?.description() ?? "")")
        })
    }
    
     @objc func buttonTapped(sender: UIButton) {
           print("Button was tapped")
       }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var button: UIButton!
    
    private func handle(persons: [Person]) {
        
        //Issue demonstrating https://github.com/JetBrains/kotlin-native/issues/2470 and https://github.com/JetBrains/kotlin-native/issues/2443
        
        let otherPersons = persons.map { (p) -> Person in
            return p
        }
        
        let otherArray: [Person] = Array(otherPersons)
        
        DispatchQueue.global(qos: .background).async {
            print("This is run on the background queue")
            
            otherArray.forEach { person in
                print("Person = \(person.firstName)")
            }
            
            DispatchQueue.main.async {
                print("This is run on the main queue, after the previous code in outer block")
                
                persons.forEach { person in
                    print("Person = \(person.firstName)")
                }
            }
        }
    }
}
