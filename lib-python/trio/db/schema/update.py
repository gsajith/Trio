import os
import glob
#import test
#import schema

def import_schema_files(schemas):
    # Finds and imports all schema_*.py files into the list
    module_files = glob.glob(os.path.dirname(os.path.abspath(__file__)) + "/schema_*.py")
    for mod in module_files:
        module = os.path.basename(os.path.splitext(mod)[0])
        newmod = __import__(module)
        schemas.append( (module, newmod) )

def main():
    # Update the database schema to the latest version
    #schema_version = db.meta.get_schema_version()
    schema_version = 1
    schemas = []
    import_schema_files(schemas)
    schemas = sorted(schemas, key=lambda tup: tup[0])
    for i in range(schema_version, len(schemas)):
        # schemas[0] is v1, schemas[1] is v2, etc
        schemas[i][1].update()
    

if __name__ == "__main__":
    main()

