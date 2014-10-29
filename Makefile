
install-third-party:
# Install the python modules
	find . -maxdepth 3 -name "setup.py" -execdir sh -c "cd `dirname {}` && python {} install" \;

start:
	PYTHONPATH=$(shell pwd)/lib-python python web/web.py
