

API {
	
	classvar all;
	var name,functions;
	var oscResponders;
	
	*new { arg name;
		^(all.at(name.asSymbol) ?? {super.new.init(name.asSymbol)});
	}
	init { arg n;
		name = n;
		functions = Dictionary.new;
		all.put(name,this);
	}
	*initClass {
		all = IdentityDictionary.new;
	}

	// defining
	add { arg selector,func;
		functions.put(selector,func)
	}
	addAll { arg dict;
		functions.putAll(dict)
	}
	make { arg func;
		this.addAll(Environment.make(func))
	}
	exposeMethods { arg obj, selectors;
		selectors.do({ arg m;
			this.add(m,{ arg ... args; obj.performList(m,args) })
		})
	}
	exposeAllExcept { arg obj,selectors=#[];
		obj.class.methods.do({ arg meth;
			if(selectors.includes(meth.name).not,{
				this.add(meth.name,{ arg ... args; obj.performList(meth.name,args) })
			})
		})
	}	
	
	// calling
	call { arg selector ... args;
		var m;
		m = functions[selector] ?? {Error(selector.asString + "not found in API" + name).throw};
		^m.valueArray(args)
	}
	// create a function
	func { arg selector ... args;
		^{ arg ... ags; this.call(selector,*(args ++ ags)) }
	}
	// respond as though declared functions were native methods to this object
	doesNotUnderstand { arg selector ... args;
		^this.call(selector,*args)
	}
	
	// OSC
	mountOSC { arg baseCmdName,addr;
		// default: this.name, nil addr = from anywhere 
		this.unmountOSC;
		functions.keysValuesDo({ arg k,f;
			var r;
			r = OSCresponderNode(addr,
					("/" ++ (baseCmdName ? name).asString ++ "/" ++ k.asString).asSymbol,
					{ arg time,resp,message,addr;
						var result,back;
						result = this.call(k,*message);
						// provisional. needs a better way to respond
						back = ['/response'] ++ result.asArray;
						addr.sendMsg(*back);
						// note: processing still didn't get the response
						// not sure where the problem is yet
					}).add;
			oscResponders = oscResponders.add( r );
		})
		^oscResponders
	}
	unmountOSC {
		oscResponders.do(_.remove);
		oscResponders = nil;
	}		
	
	printOn { arg stream;
		stream << this.class.asString << "('" << name << "')"
	}
	

}


	
	