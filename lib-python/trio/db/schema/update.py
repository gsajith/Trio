import os
import glob
#import test
#import schema

#schema_version = db.meta.get_schema_version()
def import_schema_files():
    # Finds and imports all schema_*.py files
    print os.path.dirname(__file__)
    print os.path.dirname(__file__) + "/schema_*.py"
    module_files = glob.glob(os.path.dirname(os.path.abspath(__file__)) + "/schema_*.py")
    for mod in module_files:
        module = os.path.basename(os.path.splitext(mod)[0])
        globals()[module] = __import__(module)

def main():
    # Update the database schema to the latest version
    import_schema_files()
    schema_1.show_cake()
    

if __name__ == "__main__":
    main()

