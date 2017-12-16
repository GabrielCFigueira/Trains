all:
	(cd po-uuilib; make $(MFLAGS) all)
	(cd mmt-core; make $(MFLAGS) all)
	(cd mmt-app; make $(MFLAGS) all)
	(export CLASSPATH=po-uuilib/po-uuilib.jar:mmt-core/mmt-core.jar:mmt-app/mmt-app.jar)

clean:
	(cd po-uuilib; make $(MFLAGS) clean)
	(cd mmt-core; make $(MFLAGS) clean)
	(cd mmt-app; make $(MFLAGS) clean)

install:
	(cd po-uuilib; make $(MFLAGS) install)
	(cd mmt-core; make $(MFLAGS) install)
	(cd mmt-app; make $(MFLAGS) install)
